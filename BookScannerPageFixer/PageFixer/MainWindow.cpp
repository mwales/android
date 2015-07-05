#include <QWheelEvent>
#include <QGraphicsView>
#include <QtDebug>
#include "MainWindow.h"
#include "ui_MainWindow.h"

MainWindow::MainWindow(QString imageFile, QWidget *parent) :
   QMainWindow(parent),
   ui(new Ui::MainWindow)
{
   ui->setupUi(this);

   ui->thePic->setPicture(imageFile);

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

   //QString pageDescription = QString("(%1, %2) and (%3, %4)").arg(start.x()).arg(start.y()).arg(end.x()).arg(end.y());
   QString pageDescription = pagePointsToString(PagePoints(start, end));

   //pageDescription << "(" << start.x() << ", " << start.y() << ") and (" << end.x() << ", " << end.y() << ")";

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
