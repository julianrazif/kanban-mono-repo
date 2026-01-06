package com.julian.razif.kanban.common.util;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * Utility class for validation operations.
 * Provides methods for common validation tasks to reduce code duplication.
 */
public class ValidationUtils {

  private ValidationUtils() {
  }

  /**
   * Validates that an object is not null, throwing a specified exception if it is.
   *
   * @param object            the object to check
   * @param exceptionSupplier the supplier of the exception to throw
   * @param <T>               the type of the object
   * @param <X>               the type of the exception
   * @return the validated object
   * @throws X if the object is null
   */
  public static <T, X extends Throwable> T requireNonNull(
    @Nullable T object,
    @Nonnull Supplier<X> exceptionSupplier) throws X {

    if (object == null) {
      throw exceptionSupplier.get();
    }

    return object;
  }

  /**
   * Validates that a string is not blank (null, empty, or whitespace), throwing a specified exception if it is.
   *
   * @param str               the string to check
   * @param exceptionSupplier the supplier of the exception to throw
   * @param <X>               the type of the exception
   * @return the validated string
   * @throws X if the string is blank
   */
  public static <X extends Throwable> String requireNonBlank(
    @Nullable String str,
    @Nonnull Supplier<X> exceptionSupplier) throws X {

    if (!StringUtils.hasText(str)) {
      throw exceptionSupplier.get();
    }

    return str;
  }

}
