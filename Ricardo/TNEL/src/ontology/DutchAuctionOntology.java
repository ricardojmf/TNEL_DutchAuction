package ontology;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class DutchAuctionOntology extends BeanOntology{

	private static final long serialVersionUID = -8712580898994250931L;
	
	private static Ontology theInstance = new DutchAuctionOntology("dutch_auction");

	public static Ontology getInstance(){
		return theInstance;
	}

	public DutchAuctionOntology(String name) {
		super(name);

		try {
			//add all ontology classes in this package
			add(this.getClass().getPackage().getName());

		} catch(BeanOntologyException boe) {
			boe.printStackTrace();
		}
	}
}
