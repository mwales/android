#ifndef SERVERCONTROLDIALOG_H
#define SERVERCONTROLDIALOG_H

#include <QDialog>
#include <QTcpServer>
#include <QThread>
#include <QList>
#include <QMap>
#include <QListWidgetItem>
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

   // UI handlers
   void chooseDirectory();

   void close();

   void stopServer();

   void startServer();

   void displayImage(QListWidgetItem * item);

   // Server socket related signals/slots
   void newConnection();

   void serverError(QAbstractSocket::SocketError err);

   // Signals associated with the ImageTransfer class
   void jobImageSize(QString filepath, int size);

   void jobTransferComplete(QString filepath);

   void jobError(QString filepath, QString message);




private:
   Ui::ServerControlDialog *ui;

   QTcpServer theServerSocket;

   int thePortNumber;

   QThread* theServerThread;

   QList<ImageTransfer*> theTransferJobs;

   /**
    * The keys are the line numbers in the UI list.  The string is the picture that is associated with that line
    * number.  Not all line numbers have pictures.
    */
   QMap<int, QString> theListFiles;
};

#endif // SERVERCONTROLDIALOG_H
