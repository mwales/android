#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QGraphicsScene>
#include <QGraphicsView>
#include <QMap>
#include <QList>
#include <QPair>
#include <QPoint>

typedef QPair<QPoint, QPoint> PagePoints;

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
   Q_OBJECT

public:
   explicit MainWindow(QString imageFile, QWidget *parent = 0);

   ~MainWindow();

private slots:

   void updateStatusBar(QGraphicsView::DragMode dm);

   void rectangleSelection(QPoint start, QPoint end);

   void deletePageSelection();

   void movePageSelectionUp();

   void movePageSelectionDown();

private:

   QString pagePointsToString(PagePoints pp);

   QList<PagePoints> thePagePointsList;

   Ui::MainWindow *ui;

   QString theFilename;

   QGraphicsScene theGs;

   QMap<QGraphicsView::DragMode, QString> theDragModeStrings;
};

#endif // MAINWINDOW_H
