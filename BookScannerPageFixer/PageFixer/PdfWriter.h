#ifndef PDFWRITER_H
#define PDFWRITER_H

#include "DocumentWriter.h"

class PdfWriter : public DocumentWriter
{

   Q_OBJECT
public:
   PdfWriter(QObject *parent = 0);

   virtual bool processImage(QPixmap const & imageData);
};

#endif // PDFWRITER_H
