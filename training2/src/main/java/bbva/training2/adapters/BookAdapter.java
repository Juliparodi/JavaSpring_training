package bbva.training2.adapters;

import bbva.training2.external.dto.BookDTO;
import bbva.training2.models.Book;
import bbva.training2.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.atomic.AtomicReference;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookAdapter {

    @Autowired
    BookRepository bookRepository;

    public BookDTO createBookDTO(String isbn, JsonNode request) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setIsbn(isbn);
        bookDTO.setTitle(request.path("title").textValue());
        bookDTO.setSubtitle(request.path("subtitle").textValue());
        bookDTO.setPublishers(convertJsonPublisherNodeToString(request.path("publishers")));
        bookDTO.setPublishDate(request.path("publish_date").textValue());
        bookDTO.setNumberPages(request.path("number_of_pages").intValue());
        bookDTO.setAuthors(convertJsonAuthorNodeToString(request.path("authors")));
        return bookDTO;
    }

    private String convertJsonPublisherNodeToString(JsonNode publishersNode) {
        AtomicReference<String> publishers = new AtomicReference<>("");
        publishersNode.forEach(publisherNode -> publishers
            .set(publishers.get().concat(publisherNode.path("name").textValue().concat(", "))));
        return publishers.get().substring(0, publishers.get().length() - 2);
    }

    private String convertJsonAuthorNodeToString(JsonNode authorsNode) {
        AtomicReference<String> authors = new AtomicReference<>("");
        authorsNode.forEach(x -> authors
            .set(authors.get().concat(x.path("name").textValue().concat(", "))));
        return authors.get().substring(0, authors.get().length() - 2);
    }

    public Book transformBookDTOToBook(@Valid BookDTO bookDTO) {
        String author = String.join("", bookDTO.getAuthors());
        Book book = new Book("null", author, "null", bookDTO.getTitle(), bookDTO.getSubtitle(),
            bookDTO.getPublishers(), bookDTO.getPublishDate(), bookDTO.getNumberPages(),
            bookDTO.getIsbn());
       return bookRepository.save(book);
    }
}

     /*
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

