#include "ImageTransfer.h"
#include <QtDebug>

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
           this, SLOT(error(QAbstractSocket::SocketError)));

}

void ImageTransfer::readyRead()
{
   qDebug() << __PRETTY_FUNCTION__ << "size" << theDataStream->bytesAvailable();

   if ( (theImageSize == 0) && (theDataStream->bytesAvailable() >= sizeof(quint32)) )
   {
      // Read the image size
      theDataStream->read((char*) &theImageSize, sizeof(quint32));

      qDebug() << "Image Size" << theImageSize;
      /*
      qDebug() << "Image Size parsed" << imageS

      bool success;
      int sizeParsed = imageSizeData.toInt(&success);
      qDebug() << "Image Size Data" << imageSizeData.toHex();

      if (success)
      {
         qDebug() << "Image Size of" << theFilename << "is" << sizeParsed;
         theImageSize = sizeParsed;
         emit imageSize(theImageSize);
      }
      else
      {
         qDebug() << "Failed to read image size";
      }*/
   }
   else
   {
      // Surely TCP data wouldn't send a packet of less than 4 bytes to start, so just assume at this point it is
      // payload data
      theImageData.append(theDataStream->readAll());

      qDebug() << "Data recevied so far = " << theImageData.size();
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

void ImageTransfer::error(QAbstractSocket::SocketError socketError)
{
   qDebug() << __PRETTY_FUNCTION__;
}

