#ifndef DOCUMENTWRITER_H
#define DOCUMENTWRITER_H

#include <QObject>
#include <QRunnable>
#include <QList>
#include <QStringList>
#include "Common.h"

class DocumentWriter : public QObject, public QRunnable
{
   Q_OBJECT
public:
   explicit DocumentWriter(QObject *parent = 0);

   void setImageData(const QString& imagePath, const QStringList& imageList);

   void setSelectionInfo(const QList<PagePoints>& pp);

   void setOutputInfo(const QString& outputDirectory, const QString& outputPrefix);

   // Thread work task
   virtual void run();

signals:

   /// Callback for successful job completion
   void JobSuccessful();

   /// Callback in the case of a failure
   void JobFailed(QString reason);

   /**
    * Callback for job status / progress
    * @param complete Subtasks completed
    * @param total Subtasks completed
    */
   void JobPercentComplete(int complete, int total);

public slots:

protected:

   QString theImagesPath;

   QStringList theImageList;

   QList<PagePoints> theSelections;

   QString theOutputDirectory;

   QString theOutputPrefix;

};

#endif // DOCUMENTWRITER_H
