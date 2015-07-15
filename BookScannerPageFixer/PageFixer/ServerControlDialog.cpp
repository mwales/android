#include <QFileDialog>
#include <QtDebug>

#include <QMetaObject>
#include <QMetaEnum>
#include <QTcpSocket>
#include <QNetworkInterface>
#include <QMessageBox>
#include <QStackedLayout>

#include "ServerControlDialog.h"
#include "ui_ServerControlDialog.h"
#include "PictureView.h"
#include "PictureDialog.h"

ServerControlDialog::ServerControlDialog(QWidget *parent) :
   QDialog(parent),
   ui(new Ui::ServerControlDialog)
{
   ui->setupUi(this);

   connect(ui->theCloseButton, SIGNAL(clicked()),
           this, SLOT(close()));
   connect(ui->theChooseButton, SIGNAL(clicked()),
           this, SLOT(chooseDirectory()));
   connect(ui->theStartServerButton, SIGNAL(clicked()),
           this, SLOT(startServer()));
   connect(ui->theStopServerButton, SIGNAL(clicked()),
           this, SLOT(stopServer()));
   connect(ui->theList, SIGNAL(itemDoubleClicked(QListWidgetItem*)),
           this, SLOT(displayImage(QListWidgetItem*)));

   // Setup server socket connections
   connect(&theServerSocket, SIGNAL(newConnection()),
           this, SLOT(newConnection()));
   connect(&theServerSocket, SIGNAL(acceptError(QAbstractSocket::SocketError)),
           this, SLOT(serverError(QAbstractSocket::SocketError)));

}

ServerControlDialog::~ServerControlDialog()
{
   stopServer();
   delete ui;
}

void ServerControlDialog::chooseDirectory()
{
   QString text = QFileDialog::getExistingDirectory(this, "Choose Destination Directory");
   qDebug() << __PRETTY_FUNCTION__ << "Directory:" << text;
   ui->theDirectory->setText(text);
}

void ServerControlDialog::close()
{
   stopServer();
   QDialog::close();
}

void ServerControlDialog::stopServer()
{
   qDebug() << __PRETTY_FUNCTION__;

   theServerSocket.close();
   ui->theList->addItem("Stopping server");

   ui->theStartServerButton->setEnabled(true);
   ui->theStopServerButton->setEnabled(false);
}

void ServerControlDialog::startServer()
{
   thePortNumber = ui->thePortNumber->value();
   qDebug() << __PRETTY_FUNCTION__ << "on port" << thePortNumber;

   if (ui->theDirectory->text().isEmpty() || ui->theFilename->text().isEmpty() )
   {
      QMessageBox::critical(this,
                            "Incomplete Configuration",
                            "Before starting server, select output directory and output filename");
      return;
   }

   if (!theServerSocket.listen(QHostAddress::Any, thePortNumber))
   {
      qDebug() << "Server listener failed on port" << thePortNumber;
      ui->theList->addItem( QString("Failed to start server on port %1").arg(thePortNumber));

      ui->theStartServerButton->setEnabled(true);
      ui->theStopServerButton->setEnabled(false);
   }
   else
   {
      qDebug() << "Server listener started on port" << thePortNumber;
      ui->theList->addItem( QString("Server started on port %1").arg(thePortNumber));

      QList<QHostAddress> addresses = QNetworkInterface::allAddresses();
      foreach(QHostAddress curAddr, addresses)
      {
         if (!curAddr.isLoopback() && (curAddr.protocol() == QAbstractSocket::IPv4Protocol) )
         {
            qDebug() << "Listening on interface adddress: " << curAddr;
            ui->theList->addItem(QString("Listening on interface address %1").arg(curAddr.toString()));
         }
         else
         {
            qDebug() << "Listeing on interface adddress: " << curAddr
                     << (curAddr.isLoopback() ? "(loopback)" : "")
                     << (curAddr.protocol() == QAbstractSocket::IPv4Protocol ? "(IPv4)" : "(not IPv4)");
         }

      }

      ui->theStartServerButton->setEnabled(false);
      ui->theStopServerButton->setEnabled(true);

      // Start the server thread
      theServerThread = new QThread();
      theServerThread->start();
   }
}

void ServerControlDialog::newConnection()
{
   qDebug() << __PRETTY_FUNCTION__;

   while(theServerSocket.hasPendingConnections())
   {
      ui->theList->addItem("New Connection Received");
      ui->theList->scrollToBottom();

      QString imageNumString = QString("0000%1").arg(ui->theFilenameSuffix->text()).right(4);

      QString filename = QString("%1%2.jpg").arg(ui->theFilename->text()).arg(imageNumString);

      ImageTransfer* imageJob = new ImageTransfer(ui->theDirectory->text(), filename, theServerSocket.nextPendingConnection(), theServerThread );

      connect(imageJob, SIGNAL(imageSize(QString, int)),
              this, SLOT(jobImageSize(QString, int)));
      connect(imageJob, SIGNAL(transferComplete(QString)),
              this, SLOT(jobTransferComplete(QString)));
      connect(imageJob, SIGNAL(error(QString, QString)),
              this, SLOT(jobError(QString, QString)));

      theTransferJobs.append(imageJob);

      ui->theFilenameSuffix->setValue( ui->theFilenameSuffix->value() + 1);

   }
}

void ServerControlDialog::serverError(QAbstractSocket::SocketError err)
{
   qDebug() << __PRETTY_FUNCTION__;


    QString errorValue = "Unknown Error";
    QMetaObject meta = QTcpSocket::staticMetaObject;
    for (int i=0; i < meta.enumeratorCount(); ++i)
    {
        QMetaEnum m = meta.enumerator(i);
        if (m.name() == "SocketError")
        {
            errorValue = m.valueToKey(err);
            qDebug() << "Error code found: " << errorValue;
            break;
        }
    }
}


void ServerControlDialog::jobImageSize(QString filepath, int size)
{
   qDebug() << __PRETTY_FUNCTION__;

   QFileInfo fi(filepath);
   QString filename = fi.fileName();

   ui->theList->addItem(QString("Start receiving %1 (%2 bytes)").arg(filename).arg(size));
   ui->theList->scrollToBottom();

   theListFiles.insert(ui->theList->count() - 1, filepath);
}

void ServerControlDialog::jobTransferComplete(QString filepath)
{
   qDebug() << __PRETTY_FUNCTION__;

   QFileInfo fi(filepath);
   QString filename = fi.fileName();

   ui->theList->addItem(QString("Finished receiving %1").arg(filename));
   ui->theList->scrollToBottom();

   theListFiles.insert(ui->theList->count() - 1, filepath);
}

void ServerControlDialog::jobError(QString filepath, QString message)
{
   QFileInfo fi(filepath);
   QString filename = fi.fileName();

   ui->theList->addItem(QString("Error receiving %1 (%2)").arg(filename).arg(message));
   ui->theList->scrollToBottom();
   qDebug() << __PRETTY_FUNCTION__;
}

void ServerControlDialog::displayImage(QListWidgetItem * item)
{
   int row = ui->theList->row(item);

   qDebug() << "User double clicked on row" << row << "which is for" << theListFiles[row];

   if (!theListFiles[row].isEmpty())
   {
      PictureDialog* pd = new PictureDialog(theListFiles[row], this);
      pd->show();
   }

}
