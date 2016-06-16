package com.davidflex.supermarket.agents.utils;

import java.util.List;

/**
 * Helper to work with list. Gives some functions to work with list.
 *
 * @since   June 12, 2016
 * @author  Constantin MASSON
 */
public class ListHelper {

    /**
     * Check whether the given item as an instance in the given list and return its index.
     * If several element are present, return the first index found.
     * -1 returned if list is null.
     *
     * @param list  List where to check
     * @param elt   Element to check in the list
     * @return      Element index in the list or -1 if is not in this list
     */
    public static <E>int indexOfEltClass(List<E> list, E elt){
        if(list == null){
            return -1;
        }
        for (int k = 0; k < list.size(); k++) {
            if(list.get(k).getClass().getSimpleName().equals(elt.getClass().getSimpleName())){
                return k;
            }
        }
        return -1;
    }
}
