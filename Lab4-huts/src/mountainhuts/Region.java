package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {

	private final String name;
	private final TreeSet<AltitudeRange> ranges = new TreeSet<>();
	private final Map<String, Municipality> municipalities = new HashMap<>();
	private final Map<String, MountainHut> huts = new HashMap<>();
	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name = name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		for(String range : ranges) {
			this.ranges.add(AltitudeRange.fromString(range));
		}
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		for (AltitudeRange range : this.ranges) {
			if (altitude >= range.minValue && altitude <= range.maxValue) {
				return range.toString();
			}
		}
		return AltitudeRange.UNDEFINED.toString();
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return this.municipalities.values();
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return this.huts.values();
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		Municipality m = this.municipalities.get(name);

		if(m != null)
			return m;
		
		m = new Municipality(name, province, altitude);
		this.municipalities.put(name, m);
		return m;
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, 
											  Integer bedsNumber, Municipality municipality) {
		return createOrGetMountainHut(name, null, category, bedsNumber, municipality);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, 
											  Integer bedsNumber, Municipality municipality) {
		MountainHut hut = this.huts.get(name);
		if (hut != null) {
			return hut;
		}

		hut = new MountainHut(name, category, bedsNumber, municipality, altitude);
		this.huts.put(name, hut);
		return hut;
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(file);

		Region region = new Region(name);
		List<String> lines = readData(file);

		if (lines.isEmpty()) return region;

		// Salta l'intestazione
		for (int i = 1; i < lines.size(); i++) {
			String[] fields = lines.get(i).split(";");
			if (fields.length < 7) continue;

			String province = fields[0];
			String municipalityName = fields[1];
			Integer municipalityAltitude = Integer.valueOf(fields[2]);
			String hutName = fields[3];
			String hutAltitudeStr = fields[4];
			String category = fields[5];
			Integer bedsNumber = Integer.valueOf(fields[6]);

			Municipality municipality = region.createOrGetMunicipality(municipalityName, province, municipalityAltitude);

			Integer hutAltitude = hutAltitudeStr.isEmpty() ? null : Integer.valueOf(hutAltitudeStr);

			region.createOrGetMountainHut(hutName, hutAltitude, category, bedsNumber, municipality);
		}

		return region;
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().toList();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		return this.municipalities.values().stream().collect(Collectors.toMap(Municipality::getProvince, m-> 1l, Long::sum));
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		return this.municipalities.values().stream().collect(Collectors.toMap(
			Municipality::getProvince,
			m -> {
				Map<String, Long> map = new HashMap<>();
				long count = this.huts.values().stream().filter(h -> h.getMunicipality().equals(m)).count();
				map.put(m.getName(), count);
				return map;
			},
			(m1, m2) -> {
				Map<String, Long> map = new HashMap<>();
				map.putAll(m1);
				map.putAll(m2);
				return map;
			}
		));
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		Map<String, Long> result = this.ranges.stream()
				.collect(Collectors.toMap(
					r -> r.toString(),
					r -> this.huts.values().stream()
								.map(h -> h.getAltitude().orElse(h.getMunicipality().getAltitude()))
								.filter(a -> r.isWithin(a))
								.count()
				));
		
		result.put(AltitudeRange.UNDEFINED.toString(), this.huts.size() - result.values().stream().mapToLong(a -> a).sum());

		return result;
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		return this.municipalities.values().stream()
				.collect(Collectors.toMap(
					Municipality::getProvince,
					m -> this.huts.values().stream()
							.filter(h -> h.getMunicipality().equals(m))
							.mapToInt(MountainHut::getBedsNumber)
							.sum(),
					Integer::sum));
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		Map<String, Optional<Integer>> result = this.ranges.stream()
            .collect(Collectors.toMap(
                    r -> r.toString(),
                    r -> this.huts.values().stream()
                            .filter(h -> {
                                int altitude = h.getAltitude().orElse(h.getMunicipality().getAltitude());
                                return r.isWithin(altitude);
                            })
                            .map(MountainHut::getBedsNumber)
                            .max(Integer::compareTo)
            ));

		// UNDIFENED range
		// We use the maximum number of beds in huts that do not have a defined altitude
		Optional<Integer> maxForUndefined = this.huts.values().stream()
				.map(MountainHut::getBedsNumber)
				.max(Integer::compareTo);

		result.put(AltitudeRange.UNDEFINED.toString(), maxForUndefined);

		return result;
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		return this.municipalities.values().stream()
				.collect(Collectors.groupingBy(
					m -> this.huts.values().stream()
							.filter(h -> h.getMunicipality().equals(m))
							.count(),
					Collectors.mapping(Municipality::getName, Collectors.toList())))
				.entrySet().stream()
				.collect(Collectors.toMap(
					Map.Entry::getKey,
					e -> e.getValue().stream().sorted().toList()));
	}

}
