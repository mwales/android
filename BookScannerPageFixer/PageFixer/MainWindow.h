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
   explicit MainWindow(QWidget *parent = 0);

   void loadImagePath(QString imagePath);

   ~MainWindow();

private slots:

   void updateStatusBar(QGraphicsView::DragMode dm);

   void rectangleSelection(QPoint start, QPoint end);

   void deletePageSelection();

   void movePageSelectionUp();

   void movePageSelectionDown();

   void pageSelectionListChanged();

   void nextImage();

   void previousImage();

private:

   /**
    * Converts PagePoints into a string that can be printed in the user viewable list
    * @param pp Contains two QPoints
    * @return User facing string representing the two QPoints
    */
   QString pagePointsToString(PagePoints pp);

   /// This list of points should exactly match the list that the user sees
   QList<PagePoints> thePagePointsList;

   Ui::MainWindow *ui;

   QGraphicsScene theGs;

   /// Map of the status bar strings (drag mode or selection mode)
   QMap<QGraphicsView::DragMode, QString> theDragModeStrings;

   /// The images in the current folder
   QStringList theImageFiles;

   /// The current visible image
   int theCurImageIndex;
};

#endif // MAINWINDOW_H
