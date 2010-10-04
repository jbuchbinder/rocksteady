/**
 * Copyright 2010 Google Inc.

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 **/
package com.admob.rocksteady.event;

/**
 * All known Types for metric
 */
public enum Type {

  TARGETING, ADS, USTORE, R2D2, R, NODE;

  // This helps convert string parameter to Enum type.
  /**
   * Convert a String value to a Type. Default to NODE in case it can not map
   * it.
   *
   * @param value String to convert
   * @return string value to a Type. Default to NODE in case it can not map it.
   */
  public static Type convert(String value) {
    try {
      return value == null || value.trim().length() == 0 ? NODE : Enum.valueOf(Type.class, value);
    } catch (IllegalArgumentException ex) {
      return NODE;
    }
  }
}
