#include "MainWindow.h"
#include <QApplication>
#include <QtDebug>

int main(int argc, char *argv[])
{
   QApplication a(argc, argv);

   if (argc != 2)
   {
      qDebug() << "Usage: " << argv[0] << " imageFile";
      return 0;
   }

   MainWindow w(argv[1]);
   w.show();

   return a.exec();
}
