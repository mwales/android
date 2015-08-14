#-------------------------------------------------
#
# Project created by QtCreator 2015-07-02T01:18:35
#
#-------------------------------------------------

QT       += core gui network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = PageFixer
TEMPLATE = app


SOURCES += main.cpp\
        MainWindow.cpp \
    PictureView.cpp \
    DocumentWriter.cpp \
    ServerControlDialog.cpp \
    ImageTransfer.cpp \
    PictureDialog.cpp \
    PdfWriter.cpp \
    ImageWriter.cpp

HEADERS  += MainWindow.h \
    PictureView.h \
    DocumentWriter.h \
    Common.h \
    ServerControlDialog.h \
    ImageTransfer.h \
    PictureDialog.h \
    PdfWriter.h \
    ImageWriter.h

FORMS    += MainWindow.ui \
    ServerControlDialog.ui \
    PictureDialog.ui
