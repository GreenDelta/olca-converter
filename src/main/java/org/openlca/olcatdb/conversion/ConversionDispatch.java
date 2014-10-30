package org.openlca.olcatdb.conversion;

import java.io.File;
import java.util.zip.ZipFile;

import org.openlca.olcatdb.conversion.es2tocsv.ES2ToCSVSetUpConversion;

/**
 * A factory for creating conversion methods.
 * 
 * @author Michael Srocka
 * 
 */
public class ConversionDispatch {

	private ConversionDispatch() {
	}

	/**
	 * Returns a conversion method for the given attributes
	 * 
	 * @param sourceFile
	 *            the source file to be converted. the format of this file is
	 *            automatically recognized.
	 * @param targetFormat
	 *            the target format into which the given source file should be
	 *            converted
	 */
	public static Conversion createConversion(File sourceFile, File targetDir,
			XMLProcessFormat targetFormat, boolean splitCSV, String lang,
			File masterData) throws ConversionException {

		// file checks
		if (!sourceFile.exists() || sourceFile.isDirectory()) {
			throw new IllegalArgumentException(
					"The source file must be an existing file.");
		}
		if (targetDir == null || !targetDir.exists()
				|| !targetDir.isDirectory()) {
			throw new IllegalArgumentException("The target directory must "
					+ "be an existing directory.");
		}

		// check the source format
		XMLProcessFormat sourceFormat = XMLProcessFormat.fromFile(sourceFile);
		if (sourceFormat == null) {
			throw new ConversionException("Cannot identifiy the format "
					+ "of the given source file.");
		}

		if (sourceFormat == targetFormat) {
			throw new ConversionException("The source format is "
					+ "equal to the target format.");
		}

		String notImplementedMsg = "Not yet implemented.";

		// create the conversion instance
		Conversion conversion = null;
		switch (sourceFormat) {
		case EcoSpold1:
			if (targetFormat == XMLProcessFormat.EcoSpold2)
				conversion = new ES1ToES2Conversion();
			else if (targetFormat == XMLProcessFormat.ILCD)
				conversion = new ES1ToILCDConversion();
			else if (targetFormat == XMLProcessFormat.EcoSpold1_Sima_Pro)
				conversion = new ES1ToES1SimaProConversion();
			else if (targetFormat == XMLProcessFormat.CSV)
				throw new ConversionException(notImplementedMsg);
			break;
		case EcoSpold2:
			if (targetFormat == XMLProcessFormat.EcoSpold1)
				conversion = new ES2ToES1Conversion();
			else if (targetFormat == XMLProcessFormat.EcoSpold1_Sima_Pro)
				conversion = new ES2ToES1Conversion(true);
			else if (targetFormat == XMLProcessFormat.ILCD)
				conversion = new ES2ToILCDConversion();
			else if (targetFormat == XMLProcessFormat.CSV)
				conversion = new ES2ToCSVSetUpConversion(splitCSV, lang,
						masterData);
			break;
		case ILCD:
			if (targetFormat == XMLProcessFormat.EcoSpold1)
				conversion = new ILCDToES1Conversion();
			else if (targetFormat == XMLProcessFormat.EcoSpold1_Sima_Pro)
				conversion = new ILCDToES1Conversion(true);
			else if (targetFormat == XMLProcessFormat.EcoSpold2)
				conversion = new ILCDToES2Conversion();
			else if (targetFormat == XMLProcessFormat.CSV)
				throw new ConversionException(notImplementedMsg);
			break;
		case CSV:
			if (targetFormat == XMLProcessFormat.EcoSpold2)
				conversion = new CSVToES2Conversion(sourceFile);
			else if (targetFormat == XMLProcessFormat.EcoSpold1
					|| targetFormat == XMLProcessFormat.EcoSpold1_Sima_Pro
					|| targetFormat == XMLProcessFormat.ILCD)
				throw new ConversionException(notImplementedMsg);

			break;
		default:
			break;
		}

		if (conversion == null) {
			throw new ConversionException("Cannot set up conversion.");
		}

		// set the files
		conversion.setSourceFile(sourceFile);
		conversion.setTargetDir(targetDir);

		return conversion;
	}
}
