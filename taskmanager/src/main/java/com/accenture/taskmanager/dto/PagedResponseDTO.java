package com.accenture.taskmanager.dto;

import java.util.List;

/**
 * Data Transfer Object for paginated responses.
 * This class is used to send paginated data to the client.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
//Crea la clase genérica PagedResponseDTO<T> para representar respuestas paginadas. Incluye los campos content (List<T>), pageNumber (int), pageSize (int), totalElements (long), totalPages (int) y last (boolean). Añade constructor vacío, constructor con todos los campos, getters, setters e importaciones necesarias. Incluye Javadoc de clase y de todos los métodos. — generado por IA
public class PagedResponseDTO <T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

    /**
     * Default constructor.
     */
    public PagedResponseDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param content the content of the page
     * @param pageNumber the number of the page
     * @param pageSize the size of the page
     * @param totalElements the total number of elements
     * @param totalPages the total number of pages
     * @param last whether this is the last page
     */
    public PagedResponseDTO(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    /**
     * Gets the content of the page.
     *
     * @return the content of the page
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Sets the content of the page.
     *
     * @param content the content to set
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * Gets the number of the page.
     *
     * @return the number of the page
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Sets the number of the page.
     *
     * @param pageNumber the number of the page to set
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Gets the size of the page.
     *
     * @return the size of the page
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the size of the page.
     *
     * @param pageSize the size of the page to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets the total number of elements.
     *
     * @return the total number of elements
      */
   public long getTotalElements() {
       return totalElements;
   }

   /**
   * Sets the total number of elements.
   *
   * @param totalElements the total number of elements to set
   */
   public void setTotalElements(long totalElements) {
       this.totalElements = totalElements;
   }

   /**
   * Gets the total number of pages.
   *
   * @return the total number of pages
   */
   public int getTotalPages() {
       return totalPages;
   }

   /**
   * Sets the total number of pages.
   *
   * @param totalPages the total number of pages to set
   */
   public void setTotalPages(int totalPages) {
       this.totalPages = totalPages;
   }

   /**
   * Gets whether this is the last page.
   *
   * @return whether this is the last page
   */
   public boolean isLast() {
       return last;
   }

   /**
   * Sets whether this is the last page.
   *
   * @param last whether this is the last page to set
   */
   public void setLast(boolean last) {
       this.last = last;
   }
}