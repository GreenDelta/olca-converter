/*******************************************************************************
 * Copyright (c) 2007, 2008 GreenDeltaTC. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Mozilla
 * Public License v1.1 which accompanies this distribution, and is available at
 * http://www.openlca.org/uploads/media/MPL-1.1.html
 * 
 * Contributors: 
 *			GreenDeltaTC - initial API and implementation
 * 			www.greendeltatc.com 
 *			tel.: +49 30 4849 6030 
 *			mail: gdtc@greendeltatc.com
 ******************************************************************************/

package org.openlca.olcatdb.ecospold1;

import org.openlca.olcatdb.parsing.Context;
import org.openlca.olcatdb.parsing.ContextField;
import org.openlca.olcatdb.parsing.ContextObject;
import org.openlca.olcatdb.parsing.ContextField.Type;

/**
 * Contains information about author(s), title, kind of publication, place of
 * publication, name of editors (if any), etc..
 */
@Context(name = "source", parentName = "modellingAndValidation")
public class ES1Source extends ContextObject {

	/**
	 * List of additional authors (surname and abbreviated name, e.g. Newton
	 * I.), separated by commas. 'Et al.' may be used, if more than five
	 * additonal authors contributed to the cited publication.
	 */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "additionalAuthors", 
			type = Type.Text, 
			length = 255)
	public String additionalAuthors;

	/**
	 * Indicates the first author by surname and abbreviated name (e.g.,
	 * Einstein A.). In case of measurement on site, oral communication,
	 * personal written communication and questionnaries ('sourceType'=4, 5, 6,
	 * 7) the name of the communicating person is mentioned here.
	 */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "firstAuthor", 
			type = Type.Text,
			length = 40,
			isRequired = true)
	public String firstAuthor;

	/** Indicates the issue number of the journal an article is published in. */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "issueNo", 
			type = Type.Text,
			length = 40)
	public String issueNo;

	/** Indicates the name of the journal an article is published in. */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "journal", 
			type = Type.Text,
			length = 40)
	public String journal;

	/** Contains the names of the editors (if any). */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "nameOfEditors", 
			type = Type.Text,
			length = 40)
	public String nameOfEditors;

	/** ID number to identify the source within one dataset. */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "number", 
			type = Type.Integer, 
			isRequired = true)
	public Integer number;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "pageNumbers", 
			type = Type.Text,
			length = 15)
	public String pageNumbers;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "placeOfPublications", 
			type = Type.Text,
			length  = 40)
	public String placeOfPublications;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "publisher", 
			type = Type.Text,
			length = 40)
	public String publisher;

	/**
	 * The source type. The EcoSpold codes are: 0=Undefined (default),
	 * 1=Article, 2=Chapters in anthology, 3=Separate publication, 4=Measurement
	 * on site, 5=Oral communication, 6=Personal written communication,
	 * 7=Questionnaries
	 */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "sourceType", 
			type = Type.Integer,
			isRequired = true)
	public Integer sourceType;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "text", 
			type = Type.Text,
			length = 32000)
	public String text;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "title", 
			type = Type.Text,
			isRequired = true,
			length = 32000)
	public String title;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "titleOfAnthology", 
			type = Type.Text,
			length = 255)
	public String titleOfAnthology;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "volumeNo", 
			type = Type.Integer)
	public Integer volumeNo;

	/**  */
	@ContextField(
			name = "source", 
			parentName = "modellingAndValidation", 
			isAttribute = true, 
			attributeName = "year", 
			type = Type.Integer,
			isRequired = true)
	public Integer year;
}
