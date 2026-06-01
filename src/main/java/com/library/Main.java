package com.library;

import com.library.controller.LibraryController;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.service.LibraryService;
import com.library.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepository();
        MemberRepository memberRepository = new MemberRepository();
        LibraryService service = new LibraryService(bookRepository, memberRepository);
        ConsoleView view = new ConsoleView();
        LibraryController controller = new LibraryController(service, view);
        controller.start();
    }
}
