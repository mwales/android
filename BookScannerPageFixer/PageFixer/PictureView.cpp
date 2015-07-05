#include <QtDebug>
#include <QWheelEvent>
#include "PictureView.h"

PictureView::PictureView(QWidget* parent):
   QGraphicsView(parent),
   theCurrentDragMode(QGraphicsView::RubberBandDrag),
   theScale(1.0)
{
   theGs = new QGraphicsScene();
}

PictureView::~PictureView()
{
   theGs->deleteLater();
}

void PictureView::setPicture(QString filename)
{
   theGs->addPixmap(QPixmap(filename));

   setScene(theGs);

   setInteractive(true);
   setDragMode(QGraphicsView::RubberBandDrag);

   setRubberBandSelectionMode(Qt::ContainsItemShape);

   //scale(0.5, 0.5);
}

void PictureView::wheelEvent(QWheelEvent* ev)
{
//   qDebug() << __PRETTY_FUNCTION__ << ": ev deltaAngle" << ev->angleDelta().rx() << ", " << ev->angleDelta().ry()
//               << " and " << ev->angleDelta().x() << ", " << ev->angleDelta().y();

   if (ev->angleDelta().ry() < 0)
   {
      qDebug() << "Zoom out";
      theScale = /*theScale */ 0.9;
   }
   else
   {
      qDebug() << "Zoom in";
      theScale = /*theScale */ 1.2;
   }

   scale(theScale, theScale);
}

void PictureView::mousePressEvent(QMouseEvent * event)
{
   //qDebug() << "Mouse button pressed";

   if (event->button() == Qt::MidButton)
   {
      qDebug() << "Middle mouse button pressed";
      toggleDragMode();
   }\

   QPointF scenePoint = mapToScene(event->pos());

   theRectStart = QPoint( (int) scenePoint.x(), (int) scenePoint.y() );

   qDebug() << "Clicked on " << scenePoint << " which is " << theRectStart;

   QGraphicsView::mousePressEvent(event);
}

void PictureView::mouseReleaseEvent(QMouseEvent * event)
{
   //qDebug() << "Mouse button released";

   if ( (theCurrentDragMode == QGraphicsView::RubberBandDrag) &&
        (event->button() != Qt::MidButton) )
   {
      QPointF scenePoint = mapToScene(event->pos());

      QPoint rectStop = QPoint( (int) scenePoint.x(), (int) scenePoint.y() );

      emit rectangleSelected(theRectStart, rectStop);
   }

   QPointF scenePoint = mapToScene(event->pos());

   QPoint rectStop = QPoint( (int) scenePoint.x(), (int) scenePoint.y() );

   QGraphicsView::mouseReleaseEvent(event);
}

void PictureView::toggleDragMode()
{
   if (theCurrentDragMode == QGraphicsView::RubberBandDrag)
   {
      theCurrentDragMode = QGraphicsView::ScrollHandDrag;
   }
   else
   {
      theCurrentDragMode = QGraphicsView::RubberBandDrag;
   }

   setDragMode(theCurrentDragMode);
   emit scrollModeChanged(theCurrentDragMode);

}
