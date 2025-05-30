package nyla.solutions.global.json;

import java.math.BigDecimal;

import com.google.gson.GsonBuilder;

import nyla.solutions.core.patterns.creational.BuilderDirector;
import nyla.solutions.core.patterns.iteration.Paging;


public class GsonBuilderDirector implements BuilderDirector<GsonBuilder>
{
	@Override
	public void construct(GsonBuilder builder)
	{
		GSONDateSerializer jsonDateSerializer = new GSONDateSerializer();
		GSONNumberSerializer numberSerializer = new GSONNumberSerializer();
		GSONBooleanSerializer booleanSerializer = new GSONBooleanSerializer();
		
		builder.setDateFormat(JSON.DATE_FORMAT)
		   .registerTypeAdapter(java.util.Date.class, jsonDateSerializer)
		   .registerTypeAdapter(java.sql.Date.class, jsonDateSerializer)
		   .registerTypeAdapter(java.sql.Timestamp.class, jsonDateSerializer)
		   .registerTypeAdapter(java.lang.Boolean.class, booleanSerializer)
		   .registerTypeAdapter(boolean.class, booleanSerializer)
		   .registerTypeAdapter(int.class, numberSerializer)
		   .registerTypeAdapter(java.lang.Integer.class, numberSerializer)
		   .registerTypeAdapter(long.class, numberSerializer)
		   .registerTypeAdapter(java.lang.Long.class, numberSerializer)
		   .registerTypeAdapter(float.class, numberSerializer)
		   .registerTypeAdapter(java.lang.Float.class, numberSerializer)
		   .registerTypeAdapter(double.class, numberSerializer)
		   .registerTypeAdapter(java.lang.Double.class, numberSerializer)
		   .registerTypeAdapter(short.class, numberSerializer)
		   .registerTypeAdapter(java.lang.Short.class, numberSerializer)
		   .registerTypeAdapter(BigDecimal.class, numberSerializer);
		
		//Paging<Molecule> paging = new PagingCollection(new ArrayList<Molecule>(), new PageCriteria());
		
		builder.registerTypeHierarchyAdapter(Paging.class, new PagingSerializer<Object>());
		
	}
}
