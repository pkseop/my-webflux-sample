package my.spring.sample.webflux.utils;

import my.spring.sample.webflux.enums.SortType;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

public class MongoQueryUtil {

	public static Criteria addCriteria(Criteria criteria, String name) {
		if(criteria == null) {
			return Criteria.where(name);
		} else {
			return criteria.and(name);
		}
	}

	public static Sort retrieveSort(String sort) {
		if(sort == null) {
			sort = SortType.DESC.getValue();
		}
		Sort.Order sortOrder = switch(sort) {
			case "ASC" -> new Sort.Order(Sort.Direction.ASC, "createdAt");
			default -> new Sort.Order(Sort.Direction.DESC, "createdAt");
		};
		return Sort.by(sortOrder);
	}
}
