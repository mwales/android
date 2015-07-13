#ifndef IMAGETRANSFER_H
#define IMAGETRANSFER_H

#include <QObject>
#include <QRunnable>
#include <QTcpSocket>
#include <QByteArray>

class ImageTransfer : public QObject
{
   Q_OBJECT
public:
   explicit ImageTransfer(QString path, QString filename, QTcpSocket* dataDevice, QObject *parent = 0);

signals:

   void imageSize(int size);

   void transferComplete();

   void error(QString message);

protected slots:

   void readyRead();

   void connected();

   void disconnected();

   void error(QAbstractSocket::SocketError socketError);

protected:

   QString theDataPath;

   QString theFilename;

   QTcpSocket* theDataStream;

   quint32 theImageSize;

   QByteArray theImageData;

};

#endif // IMAGETRANSFER_H
