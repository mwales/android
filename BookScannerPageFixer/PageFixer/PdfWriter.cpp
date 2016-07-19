#include "PdfWriter.h"
#include <QDir>
#include <QPixmap>
#include <QPageSize>

PdfWriter::PdfWriter(QObject *parent):
   DocumentWriter(parent),
   theWriter(NULL),
   thePainter(NULL),
   theOkToFlipPages(false)
{
}

PdfWriter::~PdfWriter()
{
   if(thePainter)
   {
      delete thePainter;
      thePainter = NULL;
   }

   if(theWriter)
   {
      delete theWriter;
      theWriter = NULL;
   }
}

bool PdfWriter::jobInit()
{
   theWriter = new QPdfWriter(QDir::cleanPath(QString("%1/%2.pdf").arg(theOutputDirectory).arg(theOutputPrefix)));
   theWriter->setResolution(300);
   theWriter->setPageOrientation(QPageLayout::Portrait);
   theWriter->setPageSize(QPageSize(QPageSize::Letter));

   thePainter = new QPainter(theWriter);
   return true;
}

bool PdfWriter::processImage(QPixmap const & imageData)
{
   if (theOkToFlipPages)
   {
      theWriter->newPage();
   }
   else
   {
      theOkToFlipPages = true;
   }

   QPixmap scaledPixmap = imageData.scaled(theWriter->width(),
                                           theWriter->height(),
                                           Qt::KeepAspectRatio,
                                           Qt::SmoothTransformation);

   thePainter->drawPixmap(QPoint(0,0), scaledPixmap);
   return true;

}

bool PdfWriter::documentClose()
{
   return true;
}

