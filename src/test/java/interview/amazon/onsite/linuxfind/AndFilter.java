package interview.amazon.onsite.linuxfind;

import java.util.Arrays;
import java.util.List;

public class AndFilter implements Filter{

    List<Filter> filters;

    public AndFilter(Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
    }

    @Override
    public boolean validate(MyFile file) {
        for (Filter filter : filters) {
            if (!filter.validate(file)) return false;
        }
        return true;
    }
}
