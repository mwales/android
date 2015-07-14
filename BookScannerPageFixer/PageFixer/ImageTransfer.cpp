#include "ImageTransfer.h"
#include <QtDebug>
#include <QtEndian>
#include <QFile>
#include <QDir>

ImageTransfer::ImageTransfer(QString path, QString filename, QTcpSocket* dataDevice, QObject *parent) :
   QObject(parent),
   theDataPath(path),
   theFilename(filename),
   theDataStream(dataDevice),
   theImageSize(0)
{
   // Handle the following socket signals
   connect(dataDevice, SIGNAL(connected()),
           this, SLOT(connected()));
   connect(dataDevice, SIGNAL(disconnected()),
           this, SLOT(disconnected()));
   connect(dataDevice, SIGNAL(readyRead()),
           this, SLOT(readyRead()));
   connect(dataDevice, SIGNAL(error(QAbstractSocket::SocketError)),
           this, SLOT(socketError(QAbstractSocket::SocketError)));

}

void ImageTransfer::readyRead()
{
   //qDebug() << __PRETTY_FUNCTION__ << "size" << theDataStream->bytesAvailable();

   if ( (theImageSize == 0) && (theDataStream->bytesAvailable() >= sizeof(quint32)) )
   {
      // Read the image size
      theDataStream->read((char*) &theImageSize, sizeof(quint32));

      // Convert from network order to big endian
      theImageSize = qFromBigEndian(theImageSize);

      qDebug() << "Image Size" << theImageSize;

      emit imageSize(fullImagePath(), theImageSize);
   }
   else
   {
      // Surely TCP data wouldn't send a packet of less than 4 bytes to start, so just assume at this point it is
      // payload data
      theImageData.append(theDataStream->readAll());

      //qDebug() << "Data recevied so far = " << theImageData.size();

      if (theImageData.size() == theImageSize)
      {
         qDebug() << "Image Complete!";

         // Now open the file to store the image
         QString absFilename = QString("%1%2%3").arg(theDataPath).arg(QDir::separator()).arg(theFilename);
         absFilename = QDir::cleanPath(absFilename);
         QFile destinationFile(absFilename);

         if (!destinationFile.open(QIODevice::WriteOnly))
         {
            // Fail to open the file

            QString msg = QString("Failed to open the file: %1").arg(destinationFile.errorString());
            qDebug() << msg;
            emit error(fullImagePath(), msg);

            closeAndDelete();
         }

         // Write the image data to the file
         int numBytesWritten = destinationFile.write(theImageData);

         if (numBytesWritten != theImageSize)
         {
            qDebug() << "Failed to write the full image to the file";

            QString msg = QString("Failed to write file %1.  Wrote %2 out of %3 bytes")
                          .arg(theFilename).arg(numBytesWritten).arg(theImageSize);

            qDebug() << msg;
            emit error(theFilename, msg);

            // Quit processing
            closeAndDelete();
         }
         else
         {
            qDebug() << "Successfully wrote: " << absFilename;
            emit transferComplete(fullImagePath());

            closeAndDelete();
         }


      }


   }
}

void ImageTransfer::connected()
{
   qDebug() << __PRETTY_FUNCTION__;
}

void ImageTransfer::disconnected()
{
   qDebug() << __PRETTY_FUNCTION__;
}

void ImageTransfer::socketError(QAbstractSocket::SocketError socketError)
{
   qDebug() << __PRETTY_FUNCTION__;
}

void ImageTransfer::closeAndDelete()
{
   theDataStream->close();
   deleteLater();
}

QString ImageTransfer::fullImagePath()
{
   return QDir::cleanPath(theDataPath + QDir::separator() + theFilename);
}

