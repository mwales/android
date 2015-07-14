#ifndef PICTUREVIEW_H
#define PICTUREVIEW_H

#include <QGraphicsView>
#include <QGraphicsScene>
#include <QWheelEvent>

class PictureView : public QGraphicsView
{
   Q_OBJECT

public:
   PictureView(QWidget* parent = NULL);

   ~PictureView();

   void setPicture(QString filename);

   void toggleDragMode();

   void autoFit();


signals:

   void scrollModeChanged(QGraphicsView::DragMode dm);

   void rectangleSelected(QPoint start, QPoint end);

protected:

   virtual void wheelEvent(QWheelEvent* ev);

   virtual void mousePressEvent(QMouseEvent * event);

   virtual void mouseReleaseEvent(QMouseEvent * event);

private:

   QPoint theRectStart;

   QGraphicsView::DragMode theCurrentDragMode;

   QGraphicsScene* theGs;

   double theScale;
};

#endif // PICTUREVIEW_H
