#-------------------------------------------------
#
# Project created by QtCreator 2015-07-02T01:18:35
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = PageFixer
TEMPLATE = app


SOURCES += main.cpp\
        MainWindow.cpp \
    PictureView.cpp \
    DocumentWriter.cpp

HEADERS  += MainWindow.h \
    PictureView.h \
    DocumentWriter.h \
    Common.h

FORMS    += MainWindow.ui
