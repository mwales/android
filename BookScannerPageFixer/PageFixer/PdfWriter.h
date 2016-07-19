#ifndef PDFWRITER_H
#define PDFWRITER_H

#include "DocumentWriter.h"
#include <QPdfWriter>
#include <QPainter>

class PdfWriter : public DocumentWriter
{

   Q_OBJECT
public:
   PdfWriter(QObject *parent = 0);

   ~PdfWriter();

protected:
   virtual bool jobInit();

   virtual bool processImage(QPixmap const & imageData);

   virtual bool documentClose();

   QPdfWriter* theWriter;

   QPainter* thePainter;

   bool theOkToFlipPages;
};

#endif // PDFWRITER_H
