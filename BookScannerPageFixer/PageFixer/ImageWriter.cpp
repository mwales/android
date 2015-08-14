#include <QtDebug>
#include <QDir>
#include <QFileInfo>
#include "ImageWriter.h"

ImageWriter::ImageWriter(QObject* parent):
   DocumentWriter(parent)
{
}

bool ImageWriter::processImage(QPixmap const & croppedImage)
{
   // Create the filename
   QString filenameCounter = QString("0000%1").arg(thePageCounter).right(4);
   QString suffix = QFileInfo(theCurImage).completeSuffix();
   QString fullPath = theOutputDirectory + QDir::separator() + theOutputPrefix + filenameCounter + "." + suffix;
   QString finalPath = QFileInfo(fullPath).absoluteFilePath();

   if (!croppedImage.save(finalPath))
   {
      // Save failed
      emit JobFailed(QString("Failed to save %1").arg(finalPath));
      return false;
   }

   qDebug() << "Final Path=" << finalPath;

   return true;
}
