#ifndef SERVERCONTROLDIALOG_H
#define SERVERCONTROLDIALOG_H

#include <QDialog>

namespace Ui {
class ServerControlDialog;
}

class ServerControlDialog : public QDialog
{
   Q_OBJECT

public:
   explicit ServerControlDialog(QWidget *parent = 0);
   ~ServerControlDialog();

private:
   Ui::ServerControlDialog *ui;
};

#endif // SERVERCONTROLDIALOG_H
