package com.library;

import com.library.controller.LibraryController;
import com.library.service.LibraryService;
import com.library.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        LibraryService service = new LibraryService();
        ConsoleView view = new ConsoleView();
        LibraryController controller = new LibraryController(service, view);
        controller.start();
    }
}
