# Introduction
In a nutshell, ACS is a discrete event based simulator for cloud environments that
aims to provide full support for the simulation of any Cloud Computing Aspect.

# About
Acs is part of a research project in the [University of Oran1 Ahmed Benbella (Algeria)](http://www.univ-oran.dz/).
Acs was developed by Samy Sadi (samy.sadi.contact at gmail) with the
supervision of Belabbas Yagoubi (byagoubi at gmail).

# License
ACS' code is published under the [GNU General Public License, version 3](http://www.gnu.org/licenses/gpl.txt)

# Downloads
ACS' Jar file can be downloaded [from here](http://samysadi.github.io/acs/releases).
Sample configuration can be found [here](http://samysadi.github.io/acs/releases).

# Documentation
The Html java documentation can be found [here](http://samysadi.github.io/acs/doc).

# Contributing
We accept Pull Requests. Please use [developer mailing list][ml] as the main channel of communication for contributors.

Also make sure to respect the next guidelines:
+ Respect the code style.
+ The simpler diffs the better. For instance make sure the code was not reformatted.
+ Create separate PR for code reformatting.
+ Make sure your changes does not break anything in existing code. In particular, make sure the existing junit tests can complete successfully.

# Usage
## Minimal working example
Download the latest jar file (see downloads section) and make sure to include it to your build path.
Download the configuration sample file (see downloads section) and unzip it somewhere in your file system.

Create a new class and write the following code:
```java
	public static void main(String[] args) throws Exception {
		Simulator simulator = FactoryUtils.generateSimulator("C:\\acs\\config\\main.config"); //assuming this is the path where you have extracted the downloaded configuration
		simulator.start();
	}
```

## Implementing your own models and algorithms
Assume you want to define a custom VirtualMachine placement policy.
The interface that defines Vm's placement is VmPlacementPolicy in the com.samysadi.acs.service.vmplacement
package.
All you have to do is define your new class that implements that interface, and add a configuration line to tell
ACS to use the new policy.

Assuming your new implementation is named MyVmPlacementPolicy in the package mypackage, then
you have to add the following configuration line (preferably in the classes.config file):
```
VmPlacementPolicy_Class=mypackage.MyVmPlacementPolicy
```

Regarding the implementation, note that you can use simply override the AbstractVmPlacementPolicy.
Such implementation will look something like this:
```java
package mypackage;
public class MyVmPlacementPolicy extends VmPlacementPolicyAbstract {
	@Override
	protected Host _selectHost(VirtualMachine vm, List<Host> poweredOnHosts, List<Host> excludedHosts) {
		Host bestHost;
		Iterator<Host> it = new ShuffledIterator<Host>(poweredOnHosts);
		for (Host candidate: poweredOnHosts) {
			if (excludedHosts != null && excludedHosts.contains(candidate))
				continue;
			if (candidate.getPowerState() == PowerState.ON) {
				final double s = computeHostScore(vm, candidate);
				if (s>0) {
					//Your own code to select the best host
				}
			}
		}
		return bestHost;
	}
}
```

## Mastering the configuration files
Configuration files can be used to modify different simulation parameters
as the network topology, the number of hosts, the configuration of those hosts and so forth.
They can also be used to define user behaviours by generating different workloads.

In another hand, you can also define which output you want to generate (and where you want the output to be saved)
using configuration files.

Besides, configuration files can be used to define custom implementations regarding different parts of ACS.

Make sure to have a look at the configurations samples to learn more about this.  

