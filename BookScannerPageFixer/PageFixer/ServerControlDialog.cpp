#include "ServerControlDialog.h"
#include "ui_ServerControlDialog.h"

ServerControlDialog::ServerControlDialog(QWidget *parent) :
   QDialog(parent),
   ui(new Ui::ServerControlDialog)
{
   ui->setupUi(this);
}

ServerControlDialog::~ServerControlDialog()
{
   delete ui;
}
