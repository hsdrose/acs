/*
===============================================================================
Copyright (c) 2014-2015, Samy Sadi. All rights reserved.
DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

This file is part of ACS - Advanced Cloud Simulator.

ACS is part of a research project undertaken by
Samy Sadi (samy.sadi.contact@gmail.com) and supervised by
Belabbas Yagoubi (byagoubi@gmail.com) in the
University of Oran1 Ahmed Benbella, Algeria.

ACS is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3
as published by the Free Software Foundation.

ACS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ACS. If not, see <http://www.gnu.org/licenses/>.
===============================================================================
*/

package com.samysadi.acs.utility.collections.infrastructure;

import java.util.Collection;
import java.util.List;

import com.samysadi.acs.hardware.Host;


/**
 * 
 * @author Samy Sadi <samy.sadi.contact@gmail.com>
 * @author Belabbas Yagoubi <byagoubi@gmail.com>
 * @since 1.0
 */
public class RackImpl extends MyArrayList<Host> implements Rack {
	private static final long serialVersionUID = 1L;
	private ClusterImpl parent;

	public RackImpl() {
		super();
	}

	public RackImpl(Collection<? extends Host> c) {
		super(c);
	}

	public RackImpl(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public List<Host> getHosts() {
		return new UnmodifiableRack(this);
	}

	private static class UnmodifiableRack extends MyUnmodifiableList<Host> implements Rack {
		public UnmodifiableRack(RackImpl rack) {
			super(rack);
		}

		@Override
		public List<Host> getHosts() {
			return this;
		}

		@Override
		public ClusterImpl getCluster() {
			return ((RackImpl) list).getCluster();
		}
	}

	@Override
	public ClusterImpl getCluster() {
		return this.parent;
	}

	public void setCluster(ClusterImpl cluster) {
		this.parent = cluster;
	}

	@Override
	public MyArrayList<?> getParent() {
		return this.parent;
	}
}
