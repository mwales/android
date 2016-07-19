#include <QThread>
#include <QtDebug>
#include <QDir>
#include <QFile>
#include <QPixmap>
#include "DocumentWriter.h"

DocumentWriter::DocumentWriter(QObject *parent) :
   QObject(parent)
{
}

void DocumentWriter::run()
{
   qDebug() << __PRETTY_FUNCTION__;

   // Preprocessing entry point
   if (!jobInit())
   {
      qDebug() << "Error while initializing for the job";
      emit JobFailed("Failed to initialize");
      return;
   }

   const int TOTAL_PAGES = theImageList.count() * theSelections.count();

   thePageCounter = 0;
   foreach(theCurImage, theImageList)
   {
      foreach(PagePoints curPP, theSelections)
      {
         int width = qAbs(curPP.first.x() - curPP.second.x());
         int height = qAbs(curPP.first.y() - curPP.second.y());

         int xVal = curPP.first.x() < curPP.second.x() ? curPP.first.x() : curPP.second.x();
         int yVal = curPP.first.y() < curPP.second.y() ? curPP.first.y() : curPP.second.y();

         QString sourcePath = QFileInfo(theImagesPath + QDir::separator() + theCurImage).absoluteFilePath();

         QPixmap sourceImage(sourcePath);

         QPixmap croppedImage = sourceImage.copy(xVal, yVal, width, height);

         if (!processImage(croppedImage))
         {
            // An error occured while processing photo, abort
            qDebug() << "Error occured while processing image " << theCurImage;
            return;
         }

         thePageCounter++;
      }

      // Update job progress
      emit JobPercentComplete(thePageCounter, TOTAL_PAGES);
   }

   if (!documentClose())
   {
      qDebug() << "Error while closing the document";
      emit JobFailed("Failed to close document");
      return;
   }

   qDebug() << __PRETTY_FUNCTION__ << " wrote " << thePageCounter << " pages";

   emit JobSuccessful();

   this->deleteLater();
}

void DocumentWriter::setImageData(const QString& imagePath, const QStringList& imageList)
{
   theImagesPath = imagePath;
   theImageList = imageList;
}

void DocumentWriter::setSelectionInfo(const QList<PagePoints>& pp)
{
   theSelections = pp;
}

void DocumentWriter::setOutputInfo(const QString& outputDirectory, const QString& outputPrefix)
{
   theOutputDirectory = outputDirectory;
   theOutputPrefix = outputPrefix;
}
