#include <QThread>
#include <QtDebug>
#include <QFile>
#include <QFileInfo>
#include <QDir>
#include "DocumentWriter.h"

DocumentWriter::DocumentWriter(QObject *parent) :
   QObject(parent)
{
}

void DocumentWriter::run()
{
   qDebug() << __PRETTY_FUNCTION__;

   const int TOTAL_PAGES = theImageList.count() * theSelections.count();

   int pageCounter = 0;
   foreach(QString curImage, theImageList)
   {
      foreach(PagePoints curPP, theSelections)
      {
         // Create the filename
         QString filenameCounter = QString("0000%1").arg(pageCounter).right(4);
         QString suffix = QFileInfo(curImage).completeSuffix();
         QString fullPath = theOutputDirectory + QDir::separator() + theOutputPrefix + filenameCounter + "." + suffix;
         QString finalPath = QFileInfo(fullPath).absoluteFilePath();

         qDebug() << "Final Path=" << finalPath;
         pageCounter++;
      }

      // Update job progress
      emit JobPercentComplete(pageCounter, TOTAL_PAGES);
   }

   qDebug() << __PRETTY_FUNCTION__ << " wrote " << pageCounter << " pages";

   emit JobSuccessful();
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
