#ifndef DOCUMENTWRITER_H
#define DOCUMENTWRITER_H

#include <QObject>
#include <QRunnable>

class DocumentWriter : public QObject, public QRunnable
{
   Q_OBJECT
public:
   explicit DocumentWriter(QObject *parent = 0);

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

};

#endif // DOCUMENTWRITER_H
