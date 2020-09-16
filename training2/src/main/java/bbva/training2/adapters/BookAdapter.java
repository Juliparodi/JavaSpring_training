package bbva.training2.adapters;

import bbva.training2.external.OpenAPI.dto.BookDTO;
import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookAdapter {

    @Autowired
    BookRepository bookRepository;

    public Book transformBookDTOToBook(BookDTO bookDTO, String isbn) {
        String author = bookDTO.getAuthors().get(0).getName();
        String publishers =
                (bookDTO.getPublishers().get(0).getName() == null || bookDTO.getPublishers().get(0)
                        .getName()
                        .isEmpty()) ? "No publishers"
                        : bookDTO.getPublishers().get(0).getName();
        Book book = new Book("null", author, "null", bookDTO.getTitle(), bookDTO.getSubtitle(),
                publishers, bookDTO.getPublishDate(), bookDTO.getNumberPages(), isbn);
        return bookRepository.save(book);
    }
}

/*
    public BookDTO createBookDTO(String isbn, JsonNode request) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn(isbn);
        bookDTO.setTitle(request.path("title").textValue());
        bookDTO.setSubtitle(request.path("subtitle").textValue());
        // bookDTO.setPublishers(convertJsonPublisherNodeToString(request.path("publishers")));
        bookDTO.setPublishDate(request.path("publish_date").textValue());
        bookDTO.setNumberPages(request.path("number_of_pages").intValue());
        // bookDTO.setAuthors(convertJsonAuthorNodeToString(request.path("authors")));
        return bookDTO;
    }
    //.stream().collect(Collectors.joining(",")

    private String convertJsonPublisherNodeToString(JsonNode publishersNode) {
        AtomicReference<String> publishers = new AtomicReference<>("");
        publishersNode.forEach(publisherNode -> publishers
                .set(publishers.get().concat(publisherNode.path("name").textValue().concat(", "))));
        return publishers.get().substring(0, publishers.get().length() - 2);
    }

    private String[] convertJsonAuthorNodeToString(JsonNode authorsNode) {
        AtomicReference<String> authors = new AtomicReference<>("");
        authorsNode.forEach(x -> authors
                .set(authors.get().concat(x.path("name").textValue().concat(", "))));
        return new String[]{authors.get().substring(0, authors.get().length() - 2)};
    }




    private String[] convertJsonNodeToArrayString(JsonNode authorsNode){
        String[] list = new String[authorsNode.size()];
        AtomicReference<Integer> count = new AtomicReference<>(0);
        authorsNode.forEach(author -> {
            list[Integer.parseInt(count.toString())] = author.path("name").textValue();
            count.set(count.get()+ 1);
        });
        return list;
    }

      */


