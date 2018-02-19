package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

@javax.persistence.Entity
public class BuyProperty extends Model {

	/** propertyIdプロパティ */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(precision = 20, nullable = false, unique = true)
	public Long propertyId;

	/** propertyCdプロパティ */
	@Column(length = 15, nullable = false, unique = true)
	public String propertyCd;

	/** propertyNameプロパティ */
	@Column(length = 100, nullable = true, unique = false)
	public String propertyName;

	/** propertyWorldLatitudeプロパティ */
	@Column(nullable = true, unique = false)
	public Double propertyWorldLatitude;

	/** propertyWorldLongitudeプロパティ */
	@Column(nullable = true, unique = false)
	public Double propertyWorldLongitude;

	public static Find<Long, BuyProperty> finder = new Find<Long, BuyProperty>() {
	};

	/**
	 * 表示範囲の緯度経度から物件情報のリストを返す
	 * 
	 * @param latFrom
	 * @param latTo
	 * @param lngFrom
	 * @param lngTo
	 * @return List<BuyProperty>
	 */
	public static List<BuyProperty> findByRage(Double latFrom, Double latTo, Double lngFrom, Double lngTo) {
		List<BuyProperty> buyPropertyList = BuyProperty.finder.where().endJunction()
				.between("propertyWorldLatitude", latFrom, latTo).between("propertyWorldLongitude", lngFrom, lngTo)
				.findList();
		return buyPropertyList;
	}
}
