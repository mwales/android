#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QGraphicsScene>
#include <QGraphicsView>
#include <QMap>
#include <QList>
#include <QPair>
#include <QPoint>
#include "Common.h"
#include "DocumentWriter.h"

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

public slots:

   void openDirectoryChooser();

private slots:

   void updateStatusBar(QGraphicsView::DragMode dm);

   void rectangleSelection(QPoint start, QPoint end);

   void deletePageSelection();

   void movePageSelectionUp();

   void movePageSelectionDown();

   void isImageProcessingAllowed();

   void nextImage();

   void previousImage();

   void processImages();
   void writePdf();

   void imageProcessingComplete();

   void imageProcessingError(QString reason);

   void imageProcessingStatus(int complete, int total);

   void showAboutDialog();

   void showAboutQtDialog();

   void startServerDialog();



private:

   /**
    * Converts PagePoints into a string that can be printed in the user viewable list
    * @param pp Contains two QPoints
    * @return User facing string representing the two QPoints
    */
   QString pagePointsToString(PagePoints pp);

   /// Updates the picture with whatever picture is indicated by theCurImageIndex
   void updatePicture();

   void startDocumentWriting(DocumentWriter* dw);

   /// This list of points should exactly match the list that the user sees
   QList<PagePoints> thePagePointsList;

   Ui::MainWindow *ui;

   QGraphicsScene theGs;

   /// Map of the status bar strings (drag mode or selection mode)
   QMap<QGraphicsView::DragMode, QString> theDragModeStrings;

   /// The images in the current folder
   QStringList theImageFiles;

   QString theImagePath;

   /// The current visible image
   int theCurImageIndex;

   /// True when images are being process (don't enable the process image button)
   bool theProcessingInProgressFlag;
};

#endif // MAINWINDOW_H
