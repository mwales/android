#include "PictureDialog.h"
#include "ui_PictureDialog.h"

PictureDialog::PictureDialog(QString filepath, QWidget *parent) :
   QDialog(parent),
   ui(new Ui::PictureDialog)
{
   ui->setupUi(this);
   setWindowTitle(filepath);
   ui->graphicsView->setPicture(filepath);
   ui->graphicsView->autoFit();

}

PictureDialog::~PictureDialog()
{
   delete ui;
}
