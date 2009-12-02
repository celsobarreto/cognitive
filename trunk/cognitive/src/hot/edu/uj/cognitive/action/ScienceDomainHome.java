package edu.uj.cognitive.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;

import edu.uj.cognitive.model.ScienceDomain;

@Name("scienceDomainHome")
public class ScienceDomainHome extends EntityHome<ScienceDomain>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8280929341304433155L;
	@RequestParameter Long scienceDomainId;

    @Override
    public Object getId()
    {
        if (scienceDomainId == null)
        {
            return super.getId();
        }
        else
        {
            return scienceDomainId;
        }
    }

    @Override @Begin
    public void create() {
        super.create();
    }

}
