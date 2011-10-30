/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

package org.apache.poi.xslf.usermodel;

import org.apache.poi.util.Beta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * For now this class renders only images supported by the javax.imageio.ImageIO
 * framework. Subclasses can override this class to support other formats, for
 * example, Use Apache batik to render WMF:
 * 
 * <pre>
 * <code>
 * @Override
 * public class MyImageRendener extends XSLFImageRendener{
 * public boolean drawImage(Graphics2D graphics, XSLFPictureData data, Rectangle2D anchor){
 * 	boolean ok = super.drawImage(graphics, data, anchor);
 * 	if(!ok){
 * 		// see what type of image we are
 * 		String contentType = data.getPackagePart().getContentType();
 * 		if(contentType.equals("image/wmf")){
 * 			// use Apache Batik to handle WMF
 * 			// see http://xmlgraphics.apache.org/batik/
 * 		}
 * 		
 * 	}
 * 	return ok;
 * }
 * }
 * </code>
 * </pre>
 * 
 * and then pass this class to your instance of java.awt.Graphics2D:
 * 
 * <pre>
 * <code>
 * graphics.setRenderingHint(XSLFRenderingHint.IMAGE_RENDERER, new MyImageRendener());
 * </code>
 * </pre>
 * 
 * @author Yegor Kozlov
 */
@Beta
public class XSLFImageRendener {

	/**
	 * Render picture data into the supplied graphics
	 * 
	 * @return true if the picture data was succesfully renderered
	 */
	public boolean drawImage(Graphics2D graphics, XSLFPictureData data,
			Rectangle2D anchor) {
		try {
			BufferedImage img = readImage(new ByteArrayInputStream(data.getData()));
			if (img != null){
                graphics.drawImage(img, (int) anchor.getX(), (int) anchor.getY(),
                        (int) anchor.getWidth(), (int) anchor.getHeight(), null);
            }
			return true;
		} catch (Exception e) {
			return false;
		}

	}

    /**
     * create a buffered image from input stream
     *
     * @return a <code>BufferedImage</code> containing the decoded
     * contents of the input, or <code>null</code>.
     */
    public BufferedImage readImage(InputStream is) throws IOException {
        return ImageIO.read(is);
    }
}