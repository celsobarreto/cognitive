package edu.uj.cognitive.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import edu.uj.cognitive.model.ScienceDomain;

@Name("scienceDomainList")
public class ScienceDomainList extends EntityQuery<ScienceDomain>
{
    public ScienceDomainList()
    {
        setEjbql("select scienceDomain from ScienceDomain scienceDomain");
    }
}
