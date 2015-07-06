#include <QWheelEvent>
#include <QGraphicsView>
#include <QtDebug>
#include <QDir>
#include <QMessageBox>

#include "MainWindow.h"
#include "ui_MainWindow.h"

MainWindow::MainWindow(QWidget *parent) :
   QMainWindow(parent),
   ui(new Ui::MainWindow),
   theCurImageIndex(0)
{
   ui->setupUi(this);



   theDragModeStrings.insert(QGraphicsView::NoDrag, "No Drag Mode");
   theDragModeStrings.insert(QGraphicsView::RubberBandDrag, "Selection Mode");
   theDragModeStrings.insert(QGraphicsView::ScrollHandDrag, "Scroll Mode");

   connect(ui->thePic, SIGNAL(scrollModeChanged(QGraphicsView::DragMode)),
           this, SLOT(updateStatusBar(QGraphicsView::DragMode)));

   connect(ui->thePic, SIGNAL(rectangleSelected(QPoint,QPoint)),
           this, SLOT(rectangleSelection(QPoint,QPoint)));

   // List manipulation button handlers
   connect(ui->theUpButton, SIGNAL(clicked()),
           this, SLOT(movePageSelectionUp()));
   connect(ui->theDownButton, SIGNAL(clicked()),
           this, SLOT(movePageSelectionDown()));
   connect(ui->theDeleteButton, SIGNAL(clicked()),
           this, SLOT(deletePageSelection()));

   // Want to know when the Process Images button should be enabled.  The rectangleSelection and deletePageSelection
   // button are the two buttons that effect the number of items in the list
   connect(ui->thePic, SIGNAL(rectangleSelected(QPoint,QPoint)),
           this, SLOT(pageSelectionListChanged()));
   connect(ui->theDeleteButton, SIGNAL(clicked()),
           this, SLOT(pageSelectionListChanged()));

   connect(ui->theNextImageButton, SIGNAL(clicked()),
           this, SLOT(nextImage()));
   connect(ui->thePreviousImageButton, SIGNAL(clicked()),
           this, SLOT(previousImage()));

   updateStatusBar(ui->thePic->dragMode());
}

MainWindow::~MainWindow()
{

   delete ui;
}

void MainWindow::updateStatusBar(QGraphicsView::DragMode dm)
{
   //qDebug() << "Drag Mode:" << theDragModeStrings[dm];

   ui->statusBar->showMessage(theDragModeStrings[dm]);
}

void MainWindow::rectangleSelection(QPoint start, QPoint end)
{
   qDebug() << "Rect Selection" << start << " and " << end;

   thePagePointsList.append( PagePoints(start, end) );

   QString pageDescription = pagePointsToString(PagePoints(start, end));

   ui->thePagePoints->addItem(pageDescription);
}

void MainWindow::deletePageSelection()
{
   int curRow = ui->thePagePoints->currentRow();
   qDebug() << __PRETTY_FUNCTION__ << ", and current row is " << curRow;

   if (curRow == -1)
   {
      // Invalid row to delete
      qDebug() << "Can't delete / none selected";
      return;
   }

   thePagePointsList.removeAt(curRow);
   ui->thePagePoints->takeItem(curRow);
}

void MainWindow::movePageSelectionUp()
{
   int curRow = ui->thePagePoints->currentRow();
   qDebug() << __PRETTY_FUNCTION__ << ", and current row is " << curRow;

   if ( (curRow == -1) || (curRow == 0) )
   {
      // Invalid row to move up
      qDebug() << "Can't move the selected row up / or none selected";
      return;
   }

   // Take the item at the curRow
   PagePoints ppTemp = thePagePointsList[curRow];
   thePagePointsList.removeAt(curRow);
   ui->thePagePoints->takeItem(curRow);

   // Insert the item at curRow - 1
   thePagePointsList.insert(curRow - 1, ppTemp);
   ui->thePagePoints->insertItem(curRow - 1, pagePointsToString(ppTemp));

   ui->thePagePoints->setCurrentRow(curRow - 1);
}

void MainWindow::movePageSelectionDown()
{
   int curRow = ui->thePagePoints->currentRow();
   qDebug() << __PRETTY_FUNCTION__ << ", and current row is " << curRow;

   if ( (curRow == -1) || (curRow == ui->thePagePoints->count() - 1) )
   {
      // Invalid row to move down
      qDebug() << "Can't move the selected row down / or none selected";
      return;
   }

   // Take the item at current row
   PagePoints ppTemp = thePagePointsList[curRow];
   thePagePointsList.removeAt(curRow);
   ui->thePagePoints->takeItem(curRow);

   // Insert the item into curRow + 1
   thePagePointsList.insert(curRow + 1, ppTemp);
   ui->thePagePoints->insertItem(curRow + 1, pagePointsToString(ppTemp));

   ui->thePagePoints->setCurrentRow(curRow + 1);
}

QString MainWindow::pagePointsToString(PagePoints pp)
{
   return QString("(%1, %2) and (%3, %4)").arg(pp.first.x()).arg(pp.first.y()).arg(pp.second.x()).arg(pp.second.y());
}

void MainWindow::loadImagePath(QString imagePath)
{
   QStringList filters;
   filters << "*.jpg" << "*.jpeg" << "*.png" << "*.bmp" << "*.ppm" << "*.xbm" << "*.xpm";
   QDir fileListing(imagePath);

   if (!fileListing.exists())
   {
      QMessageBox::critical(this, "Directory Does Not Exist", QString("Path %1 does not exist").arg(imagePath));
      return;
   }

   theImageFiles = fileListing.entryList(filters, QDir::Files | QDir::NoDotAndDotDot,QDir::Name);

   qDebug() << "Scanning the direcotry " << fileListing.absolutePath() << " for files";
   qDebug() << "Found the follwoing files: " << theImageFiles;

   if (fileListing.count() == 0)
   {
      QMessageBox::critical(this, "No images found", QString("Path %1 does not have any images").arg(imagePath));
      return;
   }

   theCurImageIndex = 0;
   ui->thePic->setPicture(theImageFiles[theCurImageIndex]);

   ui->theImagesFoundLabel->setText(QString("Images Found: %1").arg(theImageFiles.count()));
}

void MainWindow::pageSelectionListChanged()
{
   qDebug() << __PRETTY_FUNCTION__ ;

   if (thePagePointsList.count() == 0)
   {
      // Disable the process images button
      ui->theProcessImagesButton->setEnabled(false);
   }
   else
   {
      // Enabled the process images button
      ui->theProcessImagesButton->setEnabled(true);
   }
}

void MainWindow::nextImage()
{
   theCurImageIndex = (theCurImageIndex + 1) % theImageFiles.count();
   ui->thePic->setPicture(theImageFiles[theCurImageIndex]);

   qDebug() << "Setting the image to" << theImageFiles[theCurImageIndex];
}

void MainWindow::previousImage()
{
   theCurImageIndex = (theCurImageIndex - 1);

   if (theCurImageIndex == -1)
      theCurImageIndex = theImageFiles.count() - 1;

   ui->thePic->setPicture(theImageFiles[theCurImageIndex]);

   qDebug() << "Setting the image to" << theImageFiles[theCurImageIndex];
}
