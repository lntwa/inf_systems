const COLORS = ['RED', 'BLACK', 'BLUE', 'YELLOW']
const COUNTRIES = ['RUSSIA', 'UNITED_KINGDOM', 'GERMANY', 'JAPAN']

export const DIRECTORIES = {
  albums: {
    title: 'альбомы',
    url: '/api/albums',
    label: a => `${a.id} ${a.name}`,
    columns: [
      { title: 'ID', value: a => a.id },
      { title: 'название', value: a => a.name },
      { title: 'треков', value: a => a.tracks },
    ],
    fields: [
      { key: 'name', label: 'название', type: 'text' },
      { key: 'tracks', label: 'треков', type: 'number' },
    ],
    toForm: a => ({ name: a.name, tracks: a.tracks ?? '' }),
  },
  coordinates: {
    title: 'координаты',
    url: '/api/coordinates',
    label: c => `${c.id} (${c.x}; ${c.y})`,
    columns: [
      { title: 'ID', value: c => c.id },
      { title: 'X', value: c => c.x },
      { title: 'Y', value: c => c.y },
    ],
    fields: [
      { key: 'x', label: 'X', type: 'number' },
      { key: 'y', label: 'Y', type: 'number' },
    ],
    toForm: c => ({ x: c.x, y: c.y }),
  },
  locations: {
    title: 'локации',
    url: '/api/locations',
    label: l => `${l.id} (${l.x}; ${l.y}; ${l.z})`,
    nullableReplace: true,
    columns: [
      { title: 'ID', value: l => l.id },
      { title: 'X', value: l => l.x },
      { title: 'Y', value: l => l.y },
      { title: 'Z', value: l => l.z },
    ],
    fields: [
      { key: 'x', label: 'X', type: 'number' },
      { key: 'y', label: 'Y', type: 'number' },
      { key: 'z', label: 'Z', type: 'number' },
    ],
    toForm: l => ({ x: l.x, y: l.y, z: l.z }),
  },
  persons: {
    title: 'люди',
    url: '/api/persons',
    label: p => `${p.id} ${p.name}`,
    columns: [
      { title: 'ID', value: p => p.id },
      { title: 'имя', value: p => p.name },
      { title: 'цвет глаз', value: p => p.eyeColor },
      { title: 'цвет волос', value: p => p.hairColor },
      { title: 'дата рождения', value: p => p.birthday },
      { title: 'национальность', value: p => p.nationality },
      { title: 'локация', value: p => p.location ? `${p.location.id}` : '' },
    ],
    fields: [
      { key: 'name', label: 'имя', type: 'text' },
      { key: 'eyeColor', label: 'цвет глаз', type: 'select', options: COLORS },
      { key: 'hairColor', label: 'цвет волос', type: 'select', options: COLORS },
      { key: 'birthday', label: 'дата рождения', type: 'datetime-local' },
      { key: 'nationality', label: 'национальность', type: 'select', options: COUNTRIES },
      { key: 'locationId', label: 'локация', type: 'ref', ref: 'locations' },
    ],
    toForm: p => ({
      name: p.name,
      eyeColor: p.eyeColor ?? '',
      hairColor: p.hairColor ?? '',
      birthday: p.birthday ?? '',
      nationality: p.nationality ?? '',
      locationId: p.location?.id ?? '',
    }),
  },
}