#ifndef SERVERCONTROLDIALOG_H
#define SERVERCONTROLDIALOG_H

#include <QDialog>
#include <QTcpServer>
#include <QThread>
#include <QList>
#include "ImageTransfer.h"

namespace Ui {
class ServerControlDialog;
}

class ServerControlDialog : public QDialog
{
   Q_OBJECT

public:
   explicit ServerControlDialog(QWidget *parent = 0);
   ~ServerControlDialog();

private slots:

   void chooseDirectory();

   void close();

   void stopServer();

   void startServer();

   void newConnection();

   void serverError(QAbstractSocket::SocketError err);

   void jobImageSize(int size);

   void jobTransferComplete();

   void jobError(QString message);


private:
   Ui::ServerControlDialog *ui;

   QTcpServer theServerSocket;

   int thePortNumber;

   QThread* theServerThread;

   QList<ImageTransfer*> theTransferJobs;
};

#endif // SERVERCONTROLDIALOG_H
