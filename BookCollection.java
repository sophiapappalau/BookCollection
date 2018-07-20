/*
 * WWU: CS145 Fall 2017, Lab5
 *
 * BookCollection.java
 *
 * Sophia Pappalau
 *
 * Maintain a collection of books.
 *
 * Dependencies:
 *    Book.java, provided
 */

public class BookCollection {

   // A needed book cannot be found in a collection.
   public class BookNotFound extends IllegalArgumentException {
      public BookNotFound(String msg) {
         super(msg);
      }
   }

   // An attempt is being made to add a duplicate book to a collection.
   public class DuplicateBook extends IllegalArgumentException {
      public DuplicateBook(String msg) {
         super(msg);
      }
   }

   // An attempt is being made to add a book to a full collection.
   public class CollectionFull extends IllegalStateException {
      public CollectionFull(String msg) {
         super(msg);
      }
   }

   // Limit on size of collection.
   public static final int LIMIT = 200;
   
   private int capacity;
   public int size = 0;
   
   private Book[] bookList;

   // TODO: Other data fields as needed.

   /* Create an empty book collection of the given capacity.
    *
    * The capacity should not exceed the preset maximum capacity
    * given by LIMIT.
    */
   public BookCollection(int capacity) {
      this.capacity = capacity;
      bookList = new Book[this.capacity];
   }

   // Sample method: changePrice
   /* Change the price of a book.
    *
    * Change the price of the book specified by the given ISBN to
    * the given price. If the ISBN cannot be found in the collection
    * a BookNotFound exception is thrown.
    */
   public void changePrice(String isbn, double price) {
      Book theBook = findBook(isbn);
      if (theBook == null) {
         throw new BookNotFound(isbn);
      }
      theBook.setPrice(price);
   }

   /* Change the stock for a book.
    *
    * Change the stock of the book specified by the given ISBN by
    * the given quantity. The parameter quantity can be positive (books added
    * to stock) or negative (books sold or removed from stock). If the isbn
    * is not found in the collection a BookNotFound exception is thrown. If
    * the change would result in negative stock, an InsufficientStockException
    * will be thrown by the given Book object.
    */
   public void changeStock(String isbn, int quantity) {
      Book theBook = findBook(isbn);
      if(theBook == null) {
        throw new BookNotFound(isbn);
      }   
     theBook.changeStock(quantity);
   }
          
   /* Return the size--the actual number of books--in the collection.
    */
   public int getSize() {
    return size;
   }

   /* Return the total dollar value of the books in the collection.
    *
    * This is computed by adding the values for each book in the collection
    * as returned by the getStockValue method of Book.
    */
   public double getStockValue() {
      double stock = 0;
      for(int i = 0; i < bookList.length; i++)
      {
        if(bookList[i] != null) {
          stock += bookList[i].getStockValue();
        }
      }
      return stock;
   }

   /* Add a new book to the collection.
    *
    * If the collection has reached it's capacity, a CollectionFull
    * exception is thrown. If the book is already in the collection,
    * a DuplicateBook exception is thrown.
    */
   public void addBook(Book aBook) {
      Book theBook = new Book(aBook);
      if(theBook != null){
        if(findBook(theBook.getIsbn()) != null) {
          throw new DuplicateBook(theBook.getIsbn());
        }
        else if(getSize() != capacity) {
          bookList[size] = theBook;
          size++;
        }
      }
      else {
        throw new CollectionFull(theBook.getIsbn());
      }
   }

   /* Return the Book object at index i of the collection.
    *
    * If i < 0 or i >= getSize() an IndexOutOfBoundsException is thrown.
    */
   public Book objectAt(int i) {
      if(bookList[i] == null) { 
        return null;
      }
      if(i < 0 || i >= getSize()) {
        throw new IndexOutOfBoundsException();
      }
      else {
        return bookList[i];
      }
   }

   /* Merge the given book collections, returning a new collection which
    * contains all the books found in either collection.
    *
    * The capacity of the new collection will be sufficient to hold all the
    * books in the merged collection but can be larger. The collection will
    * consist of new Book objects that are distinct from the book objects in
    * the original two collections.
    *
    * When a book is found in both of collections, only one entry is added to
    * the new collection. The stock for that book is the sum of the stock in
    * the two collections and the price is the lesser of the prices for that
    * book in the two collections.
    */
   public static BookCollection merge(BookCollection coll1, BookCollection coll2) {
      int newCapacity = coll1.getSize() + coll2.getSize();
      BookCollection merged = new BookCollection(newCapacity);
      
      for(int j = 0; j < coll1.getSize(); j++) {
        Book newBook = new Book(coll1.objectAt(j));
        merged.addBook(newBook);
      }
        
      for(int i = 0; i < coll2.getSize(); i++) {
        if(merged.findBook(coll2.objectAt(i).getIsbn()) == null) {
          Book otherBook = new Book(coll2.objectAt(i));
          merged.addBook(otherBook);
        }
        else {
        Book nBook = new Book(coll2.objectAt(i));
        merged.findBook(nBook.getIsbn()).changeStock(nBook.getStock());
        merged.findBook(nBook.getIsbn()).setPrice(Math.min(merged.findBook(nBook.getIsbn()).getPrice(),
          nBook.getPrice()));
        }
       }       
     return merged;
   }

   /* Find the book with the given ISBN in this collection.
    *
    * If the book cannot be found, null is returned.
    */
   private Book findBook(String isbn) {
      for(int i = 0; i < bookList.length; i++) {
        if(bookList[i] != null) {
          if(bookList[i].getIsbn().equals(isbn))
          {
            return bookList[i];   
          }
        }  
        }
      return null;
   }
}
