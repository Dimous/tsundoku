package io.github.dimous.tsundoku.data.service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DefaultISBNRetrieverService implements IISBNRetrieverService {
    private final Pattern
        __pattern = Pattern.compile("(\\d-?){10,13}", Pattern.MULTILINE);

    public Set<String> retrieve(final String __string_contents) {
        final Set<String>
            __set_results = new HashSet<>();
        final Matcher
            __matcher = this.__pattern.matcher(__string_contents);
        ///
        ///
        while (__matcher.find()) {
            final String
                __string_i_s_b_n = __matcher.group().replaceAll("-", "");
            ///
            ///
            if (this.validateISBN10(__string_i_s_b_n) || this.validateISBN13(__string_i_s_b_n)) {
                __set_results.add(__string_i_s_b_n);
            }
        }

        return __set_results;
    }
    //---

    private boolean validateISBN10(final String __string_i_s_b_n) {
        int
            __int_checksum = 0,
            __int_multiplier = 10;
        ///
        ///
        if (__string_i_s_b_n.isEmpty()) {
            return false;
        }

        try {
            for (int __int_index = 0; __int_index < 9; ++__int_index) {
                __int_checksum += Character.getNumericValue(__string_i_s_b_n.charAt(__int_index)) * __int_multiplier;
                __int_multiplier--;
            }

            __int_checksum += 'X' == __string_i_s_b_n.charAt(9) ? 10 : Character.getNumericValue(__string_i_s_b_n.charAt(9));
        } catch (final StringIndexOutOfBoundsException __exception) {
            return false;
        }

        return 0 == __int_checksum % 11;
    }
    //---

    private boolean validateISBN13(final String __string_i_s_b_n) {
        int
            __int_checksum = 0,
            __int_multiplier = 1;
        ///
        ///
        if (__string_i_s_b_n.isEmpty()) {
            return false;
        }

        try {
            for (int __int_index = 0; __int_index < 12; ++__int_index) {
                __int_checksum += Character.getNumericValue(__string_i_s_b_n.charAt(__int_index)) * __int_multiplier;
                __int_multiplier = 1 == __int_multiplier ? 3 : 1;
            }

            __int_checksum += Character.getNumericValue(__string_i_s_b_n.charAt(12));
        } catch (final StringIndexOutOfBoundsException __exception) {
            return false;
        }

        return 0 == __int_checksum % 10;
    }
}
