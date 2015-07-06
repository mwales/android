#include "MainWindow.h"
#include <QApplication>
#include <QtDebug>

int main(int argc, char *argv[])
{
   QApplication a(argc, argv);

   MainWindow w;

   /// @todo If the user gives options for --version, show a QMessageBox::About
   /// @todo If the user gives options for --aboutqt, pop up the About Qt dialog for LGPL conformance
   /// @todo If we get really fance, make a QSettings and show About Qt as 1-time viewing dialog

   if (argc != 2)
   {
      // TODO:  Open a dialog to let user choose file path

      qDebug() << "Usage: " << argv[0] << " imagePath";
      return 0;
   }
   else
   {
      w.loadImagePath(argv[1]);
   }

   w.show();

   return a.exec();
}
