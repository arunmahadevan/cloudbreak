{% set os = grains['os'] | lower ~ grains['osmajorrelease'] %}

{% if salt['pillar.get']('hdp:stack:vdf-url') != None %}

{% if 'HDF' in salt['pillar.get']('hdp:stack:repoid') %}

create_hdf_repo:
  pkgrepo.managed:
    - name: "deb {{ salt['cmd.run']("cat /tmp/hdf-repo-url.text") }} HDF main"
    - file: /etc/apt/sources.list.d/hdf.list

{% else %}

create_hdp_repo:
  pkgrepo.managed:
    - name: "deb {{ salt['cmd.run']("cat /tmp/hdp-repo-url.text") }} HDP main"
    - file: /etc/apt/sources.list.d/hdp.list

{% endif %}

create_hdp_utils_repo:
  pkgrepo.managed:
    - name: "deb {{ salt['cmd.run']("cat /tmp/hdp-util-repo-url.text") }} HDP-UTILS main"
    - file: /etc/apt/sources.list.d/hdp-utils.list

{% else %}

create_hdp_repo:
  pkgrepo.managed:
    - name: "deb {{ salt['pillar.get']('hdp:stack:' + os) }} HDP main"
    - file: /etc/apt/sources.list.d/hdp.list

create_hdp_utils_repo:
  pkgrepo.managed:
    - name: "deb {{ salt['pillar.get']('hdp:util:' + os) }} HDP-UTILS main"
    - file: /etc/apt/sources.list.d/hdp-utils.list
{% endif %}