#ifndef IMAGEWRITER_H
#define IMAGEWRITER_H

#include "DocumentWriter.h"

class ImageWriter : public DocumentWriter
{
   Q_OBJECT
public:
   ImageWriter(QObject* parent = 0);

protected:

   virtual bool processImage(QPixmap const & croppedImage);
};

#endif // IMAGEWRITER_H
