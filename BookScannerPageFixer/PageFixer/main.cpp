#include "MainWindow.h"
#include <QApplication>
#include <QtDebug>
#include <QFileDialog>
#include <QMessageBox>

int main(int argc, char *argv[])
{
   QApplication a(argc, argv);

   MainWindow w;

   /// @todo If the user gives options for --version, show a QMessageBox::About
   /// @todo If the user gives options for --aboutqt, pop up the About Qt dialog for LGPL conformance
   /// @todo If we get really fance, make a QSettings and show About Qt as 1-time viewing dialog

   if (argc == 1)
   {
      w.openDirectoryChooser();
   }
   else
   {
      if (a.arguments().contains("--help") || a.arguments().contains("--about"))
      {
         qDebug() << "Help / About";
      }
      else if (a.arguments().contains("--aboutqt"))
      {
         qDebug() << "About Qt";
         QMessageBox::aboutQt(0);
      }
      else
      {
         // Default behavior
         w.loadImagePath(argv[1]);
      }

   }

   w.show();

   return a.exec();
}
