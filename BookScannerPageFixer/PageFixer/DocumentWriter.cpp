#include <QThread>
#include <QtDebug>
#include "DocumentWriter.h"

DocumentWriter::DocumentWriter(QObject *parent) :
   QObject(parent)
{
}

void DocumentWriter::run()
{
   qDebug() << __PRETTY_FUNCTION__ << ", going to sleep for 5 seconds";

   for(int i = 0; i < 5; i++)
   {
      QThread::sleep(1);

      emit JobPercentComplete(i+1, 5);
   }

   qDebug() << __PRETTY_FUNCTION__ << ", done sleeping";

   emit JobSuccessful();
}
