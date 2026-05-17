package tech.skullprogrammer.bguard.api.operator;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestFactory {

    public static Pageable create(int page, int size, String sortString) {
        Sort sort = Sort.unsorted();
        if(sortString != null && sortString.split(",").length == 2){
            String[] split = sortString.split(",");
            sort = Sort.by(Sort.Direction.fromOptionalString(split[1]).orElse(Sort.Direction.ASC), split[0]);
        }
        return PageRequest.of(page, size, sort);
    }
}
